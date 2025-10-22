import { useNavigate } from "react-router-dom";
import { postLogout } from "@shared/api/auth";

export default function Header() {
    const navigate = useNavigate();

    const handleLogout = async () => {
        const res = await postLogout();
        try {
            if (res) {
                alert("로그아웃되었습니다.");
                navigate("/signin");
            }
        } catch (err) {
            alert("로그아웃 실패");
        }
    };

    return (
        <header className="border-bottom">
            <nav className="navbar bg-white">
                <div className="container">
                    <span className="navbar-brand mb-0 h1">My App</span>
                    <button
                        className="btn btn-outline-danger"
                        onClick={handleLogout}
                    >
                        로그아웃
                    </button>
                </div>
            </nav>
        </header>
    );
}
