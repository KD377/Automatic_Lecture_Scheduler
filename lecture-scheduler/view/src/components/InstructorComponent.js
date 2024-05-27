import React, { useState, useEffect } from 'react';
import axios from 'axios';

const InstructorComponent = () => {
    const [name, setName] = useState('');
    const [department, setDepartment] = useState('');
    const [subjectsTaught, setSubjectsTaught] = useState([]);
    const [preferences, setPreferences] = useState([false, false, false, false, false]); // Default preferences for Mon-Fri
    const [availableSubjects, setAvailableSubjects] = useState([]);
    const [instructors, setInstructors] = useState([]);

    useEffect(() => {
        fetchSubjects();
        fetchInstructors();
    }, []);

    const fetchSubjects = () => {
        axios.get('/api/subjects')
            .then(response => setAvailableSubjects(response.data))
            .catch(error => console.error('Error fetching subjects:', error));
    };

    const fetchInstructors = () => {
        axios.get('/api/instructors')
            .then(response => setInstructors(response.data))
            .catch(error => console.error('Error fetching instructors:', error));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const selectedSubjects = subjectsTaught.map(id => availableSubjects.find(subject => subject.id === parseInt(id)));

        axios.post('/api/instructors', { name, department, subjectsTaught: selectedSubjects, preferences })
            .then(response => {
                console.log('Instructor added:', response.data);
                setName('');
                setDepartment('');
                setSubjectsTaught([]);
                setPreferences([false, false, false, false, false]);
                fetchInstructors(); // Refresh the list of instructors
            })
            .catch(error => console.error('Error adding instructor:', error));
    };

    const handleSubjectChange = (subjectId) => {
        setSubjectsTaught(prevSubjectsTaught => {
            if (prevSubjectsTaught.includes(subjectId)) {
                return prevSubjectsTaught.filter(id => id !== subjectId);
            } else {
                return [...prevSubjectsTaught, subjectId];
            }
        });
    };

    const handlePreferenceChange = (index) => {
        const newPreferences = [...preferences];
        newPreferences[index] = !newPreferences[index];
        setPreferences(newPreferences);
    };

    const handleDeleteInstructor = (id) => {
        axios.delete(`/api/instructors/${id}`)
            .then(response => {
                console.log('Instructor deleted:', response.data);
                fetchInstructors();
            })
            .catch(error => console.error('Error deleting instructor:', error));
    };

    return (
        <div>
            <form onSubmit={handleSubmit} className="mb-4">
                <h3>Add Instructor</h3>
                <div className="mb-3">
                    <label htmlFor="instructorName" className="form-label">Instructor Name</label>
                    <input
                        type="text"
                        className="form-control"
                        id="instructorName"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="Instructor Name"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="department" className="form-label">Department</label>
                    <input
                        type="text"
                        className="form-control"
                        id="department"
                        value={department}
                        onChange={(e) => setDepartment(e.target.value)}
                        placeholder="Department"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Subjects Taught</label>
                    <div className="form-group" style={{ maxHeight: '150px', overflowY: 'auto', border: '1px solid #ced4da', padding: '10px' }}>
                        {availableSubjects.map(subject => (
                            <div key={subject.id} className="form-check">
                                <input
                                    type="checkbox"
                                    className="form-check-input"
                                    id={`subject-${subject.id}`}
                                    value={subject.id}
                                    checked={subjectsTaught.includes(subject.id)}
                                    onChange={() => handleSubjectChange(subject.id)}
                                />
                                <label className="form-check-label" htmlFor={`subject-${subject.id}`}>
                                    {subject.name}
                                </label>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="mb-3">
                    <label className="form-label">Preferences</label>
                    <div className="form-check">
                        <input
                            type="checkbox"
                            className="form-check-input"
                            id="mondayPreference"
                            checked={preferences[0]}
                            onChange={() => handlePreferenceChange(0)}
                        />
                        <label className="form-check-label" htmlFor="mondayPreference">Monday</label>
                    </div>
                    <div className="form-check">
                        <input
                            type="checkbox"
                            className="form-check-input"
                            id="tuesdayPreference"
                            checked={preferences[1]}
                            onChange={() => handlePreferenceChange(1)}
                        />
                        <label className="form-check-label" htmlFor="tuesdayPreference">Tuesday</label>
                    </div>
                    <div className="form-check">
                        <input
                            type="checkbox"
                            className="form-check-input"
                            id="wednesdayPreference"
                            checked={preferences[2]}
                            onChange={() => handlePreferenceChange(2)}
                        />
                        <label className="form-check-label" htmlFor="wednesdayPreference">Wednesday</label>
                    </div>
                    <div className="form-check">
                        <input
                            type="checkbox"
                            className="form-check-input"
                            id="thursdayPreference"
                            checked={preferences[3]}
                            onChange={() => handlePreferenceChange(3)}
                        />
                        <label className="form-check-label" htmlFor="thursdayPreference">Thursday</label>
                    </div>
                    <div className="form-check">
                        <input
                            type="checkbox"
                            className="form-check-input"
                            id="fridayPreference"
                            checked={preferences[4]}
                            onChange={() => handlePreferenceChange(4)}
                        />
                        <label className="form-check-label" htmlFor="fridayPreference">Friday</label>
                    </div>
                </div>
                <button type="submit" className="btn btn-primary">Add Instructor</button>
            </form>

            <div className="mt-4" style={{ marginBottom: '100px' }}>
                <h4>Existing Instructors</h4>
                <div style={{ maxHeight: '300px', overflowY: 'auto' }}>
                    <table className="table table-bordered">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Department</th>
                            <th>Subjects</th>
                            <th>Preferences</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        {instructors.map(instructor => (
                            <tr key={instructor.id}>
                                <td>{instructor.name}</td>
                                <td>{instructor.department}</td>
                                <td>
                                    <ul>
                                        {instructor.subjectsTaught.map(subject => (
                                            <li key={subject.id}>{subject.name}</li>
                                        ))}
                                    </ul>
                                </td>
                                <td>
                                    <ul>
                                        {instructor.preferences.map((pref, index) => (
                                            <li key={index}>{['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'][index]}: {pref ? 'Available' : 'Not Available'}</li>
                                        ))}
                                    </ul>
                                </td>
                                <td>
                                    <button
                                        onClick={() => handleDeleteInstructor(instructor.id)}
                                        className="btn btn-danger btn-sm"
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default InstructorComponent;
