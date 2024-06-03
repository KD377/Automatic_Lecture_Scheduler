import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import * as XLSX from 'xlsx';
import '../css/LectureScheduler.css';

const LectureSchedulerComponent = () => {
    const [schedules, setSchedules] = useState({});
    const [selectedSchedules, setSelectedSchedules] = useState([]);
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

    const handleCheckboxChange = (groupName) => {
        setSelectedSchedules(prev =>
            prev.includes(groupName) ? prev.filter(name => name !== groupName) : [...prev, groupName]
        );
    };

    const handleGoBack = () => {
        navigate('/');
    };

    const generateExcel = () => {
        const wb = XLSX.utils.book_new();

        selectedSchedules.forEach((groupName) => {
            const groupSchedules = schedules[groupName];

            const worksheetData = [
                ["Day/Time Slot", ...Array.from({ length: 5 }, (_, i) => i + 1)]
            ];

            const daysOfWeek = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"];
            const maxTimeSlot = Math.max(...groupSchedules.map(session => session.numberOfTimeSlot));

            daysOfWeek.forEach(day => {
                const row = [day];
                for (let i = 1; i <= maxTimeSlot; i++) {
                    const session = groupSchedules.find(s => s.dayOfWeek === day && s.numberOfTimeSlot === i);
                    if (session) {
                        row.push(`Subject: ${session.subjectName}, Classroom: ${session.classroom}, Lecturer: ${session.lecturer}`);
                    } else {
                        row.push("");
                    }
                }
                worksheetData.push(row);
            });

            const ws = XLSX.utils.aoa_to_sheet(worksheetData);
            XLSX.utils.book_append_sheet(wb, ws, groupName);

            const colWidths = [{ wch: 30 }, ...Array(maxTimeSlot).fill({ wch: 30 })];
            ws['!cols'] = colWidths;

        });

        XLSX.writeFile(wb, 'Selected_Schedules.xlsx');
    };

    return (
        <div className="container mt-4">
            <h1 className="text-center mb-4">Lecture Schedules</h1>
            <div className="text-center mb-4">
                <button onClick={fetchSchedules} className="btn btn-primary mx-2" disabled={loading}>
                    {loading ? 'Generating...' : 'Generate Schedule'}
                </button>
                <button onClick={generateExcel} className="btn btn-info mx-2" disabled={selectedSchedules.length === 0}>
                    Download Selected Schedules as Excel
                </button>
                <button onClick={() => navigate('/')} className="btn btn-secondary mx-2">
                    Go Back to Forms
                </button>
            </div>
            {Object.keys(schedules).length > 0 && (
                Object.keys(schedules)
                    .sort((a, b) => a.localeCompare(b))
                    .map(groupName => (
                        <GroupSchedule
                            key={groupName}
                            groupName={groupName}
                            sessions={schedules[groupName]}
                            isChecked={selectedSchedules.includes(groupName)}
                            onCheckboxChange={() => handleCheckboxChange(groupName)}
                        />
                    ))
            )}
        </div>
    );
};

const GroupSchedule = ({ groupName, sessions, isChecked, onCheckboxChange}) => {
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
            const formattedData = `Subject: ${subjectName}, Classroom: ${classroom}, Lecturer: ${lecturer}`;
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
            <input
                type="checkbox"
                checked={isChecked}
                onChange={onCheckboxChange}
            />
            <table className="table table-bordered" id={`table-${groupName}`}>
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
