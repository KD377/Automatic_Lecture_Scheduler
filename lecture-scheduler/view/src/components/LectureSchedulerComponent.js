import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../css/LectureScheduler.css';
import jsPDF from 'jspdf';

const LectureSchedulerComponent = () => {
    const [schedules, setSchedules] = useState({});
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const fetchSchedules = () => {
        setLoading(true);
        axios.get('/api/algorithm/trigger-genetic-algorithm')
            .then(response => {
                setSchedules(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("There was an error fetching the schedule data!", error);
                setLoading(false);
            });
    };

    const handleGoBack = () => {
        navigate('/');
    };

    const generatePDF = () => {
        const doc = new jsPDF();
        let yPos = 10;

        Object.keys(schedules).forEach((groupName, groupIndex) => {
            doc.text(`Schedule for ${groupName}`, 10, yPos);
            yPos += 10;

            const sessions = schedules[groupName];
            const maxTimeSlot = Math.max(...sessions.map(session => session.numberOfTimeSlot));
            const timeSlots = Array.from({ length: maxTimeSlot }, (_, i) => i + 1);
            const daysOfWeek = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"];

            daysOfWeek.forEach(day => {
                sessions.forEach(session => {
                    if (session.dayOfWeek === day) {
                        const text = `Time Slot ${session.numberOfTimeSlot}: ${session.subjectName}, ${session.classroom}, ${session.lecturer}`;
                        doc.text(text, 10, yPos);
                        yPos += 10;
                    }
                });
                yPos += 5;
            });
            yPos += 10;
        });

        doc.save('All_Schedules.pdf');
    };

    return (
        <div className="container mt-4">
            <h1 className="text-center mb-4">Lecture Schedules</h1>
            <div className="text-center mb-4">
                <button onClick={fetchSchedules} className="btn btn-primary mx-2" disabled={loading}>
                    {loading ? 'Generating...' : 'Generate Schedule'}
                </button>
                <button onClick={generatePDF} className="btn btn-info mx-2">Download All Schedules as PDF</button>
                <button onClick={handleGoBack} className="btn btn-secondary mx-2">
                    Go Back to Forms
                </button>
            </div>
            {Object.keys(schedules).length > 0 && (
                Object.keys(schedules)
                    .sort((a, b) => a.localeCompare(b))
                    .map(groupName => (
                        <GroupSchedule key={groupName} groupName={groupName} sessions={schedules[groupName]} />
                    ))
            )}
        </div>
    );
};

const GroupSchedule = ({ groupName, sessions }) => {
    const daysOfWeek = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"];

    const maxTimeSlot = Math.max(...sessions.map(session => session.numberOfTimeSlot));

    const timeSlots = Array.from({ length: maxTimeSlot }, (_, i) => i + 1);

    const generateTableData = () => {
        const tableData = {};
        daysOfWeek.forEach(day => {
            tableData[day] = Array(maxTimeSlot).fill('');
        });

        sessions.forEach(session => {
            const { dayOfWeek, numberOfTimeSlot, lecturer, classroom, subjectName } = session;
            const formattedData = `Subject: ${subjectName}<br />Classroom: ${classroom}<br />Lecturer: ${lecturer}`;
            if (tableData[dayOfWeek]) {
                tableData[dayOfWeek][numberOfTimeSlot - 1] = formattedData;
            }
        });

        return tableData;
    };

    const tableData = generateTableData();

    return (
        <div className="mb-4">
            <h3 className="text-center">{groupName}</h3>
            <table className="table table-bordered">
                <thead className="thead-light">
                <tr>
                    <th scope="col">Day/Time Slot</th>
                    {timeSlots.map(slot => (
                        <th key={slot} scope="col">{slot}</th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {daysOfWeek.map(day => (
                    <tr key={day}>
                        <td>{day}</td>
                        {timeSlots.map((slot, index) => (
                            <td key={index} dangerouslySetInnerHTML={{ __html: tableData[day][index] }} />
                        ))}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default LectureSchedulerComponent;
