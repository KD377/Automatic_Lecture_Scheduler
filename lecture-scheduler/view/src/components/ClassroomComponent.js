import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ClassroomComponent = () => {
    const [name, setName] = useState('');
    const [availableSubjects, setAvailableSubjects] = useState([]);
    const [selectedSubjects, setSelectedSubjects] = useState([]);
    const [classrooms, setClassrooms] = useState([]);

    useEffect(() => {
        fetchClassrooms();
        axios.get('/api/subjects')
            .then(response => setAvailableSubjects(response.data))
            .catch(error => console.error('Error fetching subjects:', error));
    }, []);

    const fetchClassrooms = () => {
        axios.get('/api/classrooms')
            .then(response => setClassrooms(response.data))
            .catch(error => console.error('Error fetching classrooms:', error));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('/api/classrooms', { name, subjects: selectedSubjects })
            .then(response => {
                console.log('Classroom added:', response.data);
                setName('');
                setSelectedSubjects([]);
                fetchClassrooms();
            })
            .catch(error => console.error('Error adding classroom:', error));
    };

    const handleSubjectChange = (subject) => {
        setSelectedSubjects(prevSelectedSubjects => {
            if (prevSelectedSubjects.some(s => s.id === subject.id)) {
                return prevSelectedSubjects.filter(s => s.id !== subject.id);
            } else {
                return [...prevSelectedSubjects, subject];
            }
        });
    };

    const handleDeleteClassroom = (id) => {
        axios.delete(`/api/classrooms/${id}`)
            .then(response => {
                console.log('Classroom deleted:', response.data);
                fetchClassrooms();
            })
            .catch(error => console.error('Error deleting classroom:', error));
    };

    return (
        <div>
            <form onSubmit={handleSubmit} className="mb-4">
                <h3>Add Classroom</h3>
                <div className="mb-3">
                    <label htmlFor="classroomName" className="form-label">Classroom Name</label>
                    <input
                        type="text"
                        className="form-control"
                        id="classroomName"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="Classroom Name"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Available Subjects</label>
                    <div className="form-group" style={{ maxHeight: '150px', overflowY: 'auto', border: '1px solid #ced4da', padding: '10px' }}>
                        {availableSubjects.map(subject => (
                            <div key={subject.id} className="form-check">
                                <input
                                    type="checkbox"
                                    className="form-check-input"
                                    id={`subject-${subject.id}`}
                                    value={subject.id}
                                    checked={selectedSubjects.some(s => s.id === subject.id)}
                                    onChange={() => handleSubjectChange(subject)}
                                />
                                <label className="form-check-label" htmlFor={`subject-${subject.id}`}>
                                    {subject.name}
                                </label>
                            </div>
                        ))}
                    </div>
                </div>
                <button type="submit" className="btn btn-primary">Add Classroom</button>
            </form>

            <div className="mt-4">
                <h4>Existing Classrooms</h4>
                <div style={{ maxHeight: '300px', overflowY: 'auto' }}>
                    <table className="table table-bordered">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Subjects</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        {classrooms.map(classroom => (
                            <tr key={classroom.id}>
                                <td style={{ width: '20%' }}>{classroom.name}</td>
                                <td style={{ width: '60%' }}>
                                    <ul>
                                        {classroom.subjects.map(subject => (
                                            <li key={subject.id}>{subject.name}</li>
                                        ))}
                                    </ul>
                                </td>
                                <td style={{ width: '20%' }}>
                                    <button
                                        onClick={() => handleDeleteClassroom(classroom.id)}
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

export default ClassroomComponent;
