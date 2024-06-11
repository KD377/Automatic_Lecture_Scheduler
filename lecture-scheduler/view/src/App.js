import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom';
import ClassroomComponent from './components/ClassroomComponent';
import GroupComponent from './components/GroupComponent';
import InstructorComponent from './components/InstructorComponent';
import SubjectComponent from './components/SubjectComponent';
import LectureSchedulerComponent from './components/LectureSchedulerComponent';
import { GoogleOAuthProvider, useGoogleLogin } from '@react-oauth/google';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import './css/LectureScheduler.css';

const clientId = '1075986554465-h89n6mls3cbdqsib3fa2h79ib4m0dg31.apps.googleusercontent.com';
const clientSecret = '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';
const redirectUri = 'http://localhost:3000';

function App() {
    return (
        <GoogleOAuthProvider clientId={clientId}>
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
        </GoogleOAuthProvider>
    );
}

const Home = () => {
    const [selectedComponent, setSelectedComponent] = useState(null);
    const [events, setEvents] = useState([]);
    const navigate = useNavigate();

    const handleGoToSchedulePage = () => {
        navigate('/schedule');
    };

    const login = useGoogleLogin({
        onSuccess: async tokenResponse => {
            console.log('Authorization code received:', tokenResponse.code);
            try {
                const tokenExchangeResponse = await getTokenFromCode(tokenResponse.code);
                console.log('Token response:', tokenExchangeResponse.data);
                localStorage.setItem('accessToken', tokenExchangeResponse.data.access_token);
                fetchCalendarEvents(tokenExchangeResponse.data.access_token);
            } catch (error) {
                console.error('Error fetching tokens', error);
                if (error.response) {
                    console.error('Error response data:', error.response.data);
                    console.error('Error response status:', error.response.status);
                    console.error('Error response headers:', error.response.headers);
                } else if (error.request) {
                    console.error('Error request:', error.request);
                } else {
                    console.error('Error message:', error.message);
                }
            }
        },
        flow: 'auth-code'
    });

    const getTokenFromCode = async (code) => {
        const params = new URLSearchParams();
        params.append('client_id', clientId);
        params.append('client_secret', clientSecret);
        params.append('code', code);
        params.append('grant_type', 'authorization_code');
        params.append('redirect_uri', redirectUri);

        return axios.post('https://oauth2.googleapis.com/token', params, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        });
    };

    const fetchCalendarEvents = async (accessToken) => {
        try {
            const response = await axios.get('http://localhost:8080/windows/home', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`,
                },
            });
            console.log(response.data);
        } catch (error) {
            console.error('Error fetching calendar events', error);
        }
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
            <div className="text-center mt-4">
                <button onClick={() => login()} className="btn btn-primary">Login with Google</button>
            </div>
            <div className="mt-4">
                {renderSelectedComponent()}
            </div>
            <div className="mt-4">
                <h2>Calendar Events</h2>
                <ul>
                    {events.map((event, index) => (
                        <li key={index}>{event.summary} - {event.start.dateTime} to {event.end.dateTime}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default App;
