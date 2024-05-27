import React from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom';
import ClassroomForm from './components/ClassroomComponent';
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
    const navigate = useNavigate();

    const handleGoToSchedulePage = () => {
        navigate('/schedule');
    };

    return (
        <div>
            <h2 className="text-center mt-4">Form</h2>
            <div className="text-center mt-4">
                <button onClick={handleGoToSchedulePage} className="btn btn-primary">Go to Schedule Page</button>
            </div>
            <ClassroomForm />
        </div>
    );
};

export default App;
