import React, { useState, useEffect } from 'react';
import axios from 'axios';

const GroupComponent = () => {
    const [name, setName] = useState('');
    const [programOfStudy, setProgramOfStudy] = useState('');
    const [numberOfStudents, setNumberOfStudents] = useState(0);
    const [groups, setGroups] = useState([]);
    const [editMode, setEditMode] = useState(false);
    const [editGroupId, setEditGroupId] = useState(null);

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
        const groupData = { name, programOfStudy, numberOfStudents };

        if (editMode) {
            axios.put(`/api/groups/${editGroupId}`, groupData)
                .then(response => {
                    console.log('Group updated:', response.data);
                    setEditMode(false);
                    setEditGroupId(null);
                    fetchGroups();
                })
                .catch(error => console.error('Error updating group:', error));
        } else {
            axios.post('/api/groups', groupData)
                .then(response => {
                    console.log('Group added:', response.data);
                    fetchGroups();
                })
                .catch(error => console.error('Error adding group:', error));
        }

        setName('');
        setProgramOfStudy('');
        setNumberOfStudents(0);
    };

    const handleEditGroup = (group) => {
        setName(group.name);
        setProgramOfStudy(group.programOfStudy);
        setNumberOfStudents(group.numberOfStudents);
        setEditGroupId(group.id);
        setEditMode(true);
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
                <h3>{editMode ? 'Update Group' : 'Add Group'}</h3>
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
                <button type="submit" className="btn btn-primary">{editMode ? 'Update Group' : 'Add Group'}</button>
                {editMode && (
                    <button type="button" className="btn btn-secondary ms-2" onClick={() => {
                        setEditMode(false);
                        setEditGroupId(null);
                        setName('');
                        setProgramOfStudy('');
                        setNumberOfStudents(0);
                    }}>
                        Cancel
                    </button>
                )}
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
                                        onClick={() => handleEditGroup(group)}
                                        className="btn btn-warning btn-sm me-2"
                                    >
                                        Update
                                    </button>
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
