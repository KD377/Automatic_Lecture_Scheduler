import React, { useState, useEffect } from 'react';
import axios from 'axios';

const GroupComponent = () => {
    const [name, setName] = useState('');
    const [programOfStudy, setProgramOfStudy] = useState('');
    const [numberOfStudents, setNumberOfStudents] = useState(0);
    const [groups, setGroups] = useState([]);

    useEffect(() => {
        fetchGroups();
    }, []);

    const fetchGroups = () => {
        axios.get('/api/groups')
            .then(response => setGroups(response.data))
            .catch(error => console.error('Error fetching groups:', error));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('/api/groups', { name, programOfStudy, numberOfStudents })
            .then(response => {
                console.log('Group added:', response.data);
                setName('');
                setProgramOfStudy('');
                setNumberOfStudents(0);
                fetchGroups(); // Refresh the list of groups
            })
            .catch(error => console.error('Error adding group:', error));
    };

    const handleDeleteGroup = (id) => {
        axios.delete(`/api/groups/${id}`)
            .then(response => {
                console.log('Group deleted:', response.data);
                fetchGroups();
            })
            .catch(error => console.error('Error deleting group:', error));
    };

    return (
        <div>
            <form onSubmit={handleSubmit} className="mb-4">
                <h3>Add Group</h3>
                <div className="mb-3">
                    <label htmlFor="groupName" className="form-label">Group Name</label>
                    <input
                        type="text"
                        className="form-control"
                        id="groupName"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="Group Name"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="programOfStudy" className="form-label">Program of Study</label>
                    <input
                        type="text"
                        className="form-control"
                        id="programOfStudy"
                        value={programOfStudy}
                        onChange={(e) => setProgramOfStudy(e.target.value)}
                        placeholder="Program of Study"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="numberOfStudents" className="form-label">Number of Students</label>
                    <input
                        type="number"
                        className="form-control"
                        id="numberOfStudents"
                        value={numberOfStudents}
                        onChange={(e) => setNumberOfStudents(e.target.value)}
                        placeholder="Number of Students"
                        required
                    />
                </div>
                <button type="submit" className="btn btn-primary">Add Group</button>
            </form>

            <div className="mt-4">
                <h4>Existing Groups</h4>
                <div style={{ maxHeight: '300px', overflowY: 'auto' }}>
                    <table className="table table-bordered">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Program of Study</th>
                            <th>Number of Students</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        {groups.map(group => (
                            <tr key={group.id}>
                                <td>{group.name}</td>
                                <td>{group.programOfStudy}</td>
                                <td>{group.numberOfStudents}</td>
                                <td>
                                    <button
                                        onClick={() => handleDeleteGroup(group.id)}
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

export default GroupComponent;
