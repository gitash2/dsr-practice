import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import HomePage from "./pages/HomePage";
import AddUserToGroupPage from "./pages/AddUserToGroupPage";
import CreateSharedAccountPage from "./pages/CreateSharedAccountPage";
import PayDebtPage from "./pages/PayDebtPage";
import Navbar from "./components/Navbar";
import SharedBillPage from "./pages/SharedBillPage.jsx";

function App() {
    return (
        <Router>
            <Navbar />
            <div className="container mt-4">
                <Routes>
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                    <Route path="/home" element={<HomePage />} />
                    <Route path="/add-user" element={<AddUserToGroupPage/>} />
                    <Route path="/create-account" element={<CreateSharedAccountPage/>} />
                    <Route path="/pay-debt" element={<PayDebtPage/>} />
                    <Route path="/shared-bills/:id" element={<SharedBillPage />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
