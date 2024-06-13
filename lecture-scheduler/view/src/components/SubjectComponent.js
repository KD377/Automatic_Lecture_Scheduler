import React, { useState, useEffect } from 'react';
import api from '../axiosConfig.js';

const SubjectComponent = () => {
    const [name, setName] = useState('');
    const [courseLevel, setCourseLevel] = useState('');
    const [courseLength, setCourseLength] = useState(0);
    const [subjects, setSubjects] = useState([]);
    const [editMode, setEditMode] = useState(false);
    const [editSubjectId, setEditSubjectId] = useState(null);

    useEffect(() => {
        fetchSubjects();
    }, []);

    const fetchSubjects = () => {
        api.get('/api/subjects')
            .then(response => setSubjects(response.data))
            .catch(error => console.error('Error fetching subjects:', error));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const subjectData = { name, courseLevel, courseLength };
        if (editMode) {
            api.put(`/api/subjects/${editSubjectId}`, subjectData)
                .then(response => {
                    console.log('Subject updated:', response.data);
                    setEditMode(false);
                    setEditSubjectId(null);
                    fetchSubjects();
                })
                .catch(error => console.error('Error updating subject:', error));
        } else {
            api.post('/api/subjects', subjectData)
                .then(response => {
                    console.log('Subject added:', response.data);
                    fetchSubjects();
                })
                .catch(error => console.error('Error adding subject:', error));
        }
        setName('');
        setCourseLevel('');
        setCourseLength(0);
    };

    const handleDeleteSubject = (id) => {
        api.delete(`/api/subjects/${id}`)
            .then(response => {
                console.log('Subject deleted:', response.data);
                fetchSubjects(); // Refresh the list of subjects
            })
            .catch(error => console.error('Error deleting subject:', error));
    };

    const handleEditSubject = (subject) => {
        setName(subject.name);
        setCourseLevel(subject.courseLevel);
        setCourseLength(subject.courseLength);
        setEditSubjectId(subject.id);
        setEditMode(true);
    };

    return (
        <div>
            <form onSubmit={handleSubmit} className="mb-4">
                <h3>{editMode ? 'Update Subject' : 'Add Subject'}</h3>
                <div className="mb-3">
                    <label htmlFor="subjectName" className="form-label">Subject Name</label>
                    <input
                        type="text"
                        className="form-control"
                        id="subjectName"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="Subject Name"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="courseLevel" className="form-label">Course Level</label>
                    <input
                        type="text"
                        className="form-control"
                        id="courseLevel"
                        value={courseLevel}
                        onChange={(e) => setCourseLevel(e.target.value)}
                        placeholder="Course Level"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="courseLength" className="form-label">Course Length</label>
                    <input
                        type="number"
                        className="form-control"
                        id="courseLength"
                        value={courseLength}
                        onChange={(e) => setCourseLength(e.target.value)}
                        placeholder="Course Length"
                        required
                    />
                </div>
                <button type="submit" className="btn btn-primary">{editMode ? 'Update Subject' : 'Add Subject'}</button>
                {editMode && (
                    <button type="button" className="btn btn-secondary ms-2" onClick={() => {
                        setEditMode(false);
                        setEditSubjectId(null);
                        setName('');
                        setCourseLevel('');
                        setCourseLength(0);
                    }}>
                        Cancel
                    </button>
                )}
            </form>

            <div className="mt-4">
                <h4>Existing Subjects</h4>
                <div style={{ maxHeight: '300px', overflowY: 'auto' }}>
                    <table className="table table-bordered">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Course Level</th>
                            <th>Course Length</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        {subjects.map(subject => (
                            <tr key={subject.id}>
                                <td>{subject.name}</td>
                                <td>{subject.courseLevel}</td>
                                <td>{subject.courseLength}</td>
                                <td>
                                    <button
                                        onClick={() => handleEditSubject(subject)}
                                        className="btn btn-warning btn-sm me-2"
                                    >
                                        Update
                                    </button>
                                    <button
                                        onClick={() => handleDeleteSubject(subject.id)}
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

export default SubjectComponent;
