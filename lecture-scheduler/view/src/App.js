import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom';
import ClassroomComponent from './components/ClassroomComponent';
import GroupComponent from './components/GroupComponent';
import InstructorComponent from './components/InstructorComponent';
import SubjectComponent from './components/SubjectComponent';
import LectureSchedulerComponent from './components/LectureSchedulerComponent';
import 'bootstrap/dist/css/bootstrap.min.css';
import './css/LectureScheduler.css';

function App() {
    return (
        <Router>
            <div className="App container">
                <main>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/schedule" element={<LectureSchedulerComponent />} />
                    </Routes>
                </main>
            </div>
        </Router>
    );
}

const Home = () => {
    const [selectedComponent, setSelectedComponent] = useState(null);
    const navigate = useNavigate();

    const handleGoToSchedulePage = () => {
        navigate('/schedule');
    };

    const renderSelectedComponent = () => {
        switch (selectedComponent) {
            case 'subjects':
                return <SubjectComponent />;
            case 'classrooms':
                return <ClassroomComponent />;
            case 'instructors':
                return <InstructorComponent />;
            case 'groups':
                return <GroupComponent />;
            default:
                return null;
        }
    };

    return (
        <div>
            <h1 className="text-center mt-4">Form</h1>
            <div className="text-center mt-4">
                <button onClick={handleGoToSchedulePage} className="btn btn-primary">Go to Schedule Page</button>
            </div>
            <div className="text-center mt-4">
                <button onClick={() => setSelectedComponent('subjects')} className="btn btn-secondary m-2">Subjects</button>
                <button onClick={() => setSelectedComponent('classrooms')} className="btn btn-secondary m-2">Classrooms</button>
                <button onClick={() => setSelectedComponent('instructors')} className="btn btn-secondary m-2">Instructors</button>
                <button onClick={() => setSelectedComponent('groups')} className="btn btn-secondary m-2">Groups</button>
            </div>
            <div className="mt-4">
                {renderSelectedComponent()}
            </div>
        </div>
    );
};

export default App;
