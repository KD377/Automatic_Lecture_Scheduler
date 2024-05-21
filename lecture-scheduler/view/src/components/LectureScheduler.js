import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../css/LectureScheduler.css'; // Adjust the path based on your structure

const LectureScheduler = () => {
    const [schedules, setSchedules] = useState({});

    useEffect(() => {
        axios.get('/api/algorithm/trigger-genetic-algorithm')
            .then(response => {
                setSchedules(response.data);
            })
            .catch(error => {
                console.error("There was an error fetching the schedule data!", error);
            });
    }, []);

    return (
        <div>
            {Object.keys(schedules).map(groupName => (
                <GroupSchedule key={groupName} groupName={groupName} sessions={schedules[groupName]} />
            ))}
        </div>
    );
};

const GroupSchedule = ({ groupName, sessions }) => {
    const daysOfWeek = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"];

    // Determine the maximum number of time slots for this group
    const maxTimeSlot = Math.max(...sessions.map(session => session.numberOfTimeSlot));

    // Generate an array of time slots up to the maximum number
    const timeSlots = Array.from({ length: maxTimeSlot }, (_, i) => i + 1);

    const generateTableData = () => {
        const tableData = {};
        daysOfWeek.forEach(day => {
            tableData[day] = Array(maxTimeSlot).fill('');
        });

        sessions.forEach(session => {
            const { dayOfWeek, numberOfTimeSlot, lecturer, classroom, subjectName } = session;
            const formattedData = `Subject: ${subjectName}\nClassroom: ${classroom}\nLecturer: ${lecturer}`;
            if (tableData[dayOfWeek]) {
                tableData[dayOfWeek][numberOfTimeSlot - 1] = formattedData;
            }
        });

        return tableData;
    };

    const tableData = generateTableData();

    return (
        <div>
            <h3>{groupName}</h3>
            <table className="schedule-table">
                <thead>
                <tr>
                    <th>Day/Time Slot</th>
                    {timeSlots.map(slot => (
                        <th key={slot}>{slot}</th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {daysOfWeek.map(day => (
                    <tr key={day}>
                        <td>{day}</td>
                        {timeSlots.map((slot, index) => (
                            <td key={index}>
                                {tableData[day][index]}
                            </td>
                        ))}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default LectureScheduler;
