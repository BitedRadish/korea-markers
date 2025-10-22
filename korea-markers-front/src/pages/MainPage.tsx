import { postLogout } from "@shared/api/auth";
import Header from "@shared/component/Header";

export default function MainPage() {
    return (
        <div className="min-vh-100 d-flex flex-column">
            <Header />
            <main className="container py-5">
                <div className="text-center">
                    <h2 className="mb-2">메인 페이지</h2>
                    <p className="text-muted">
                        간단한 환영 문구나 대시보드 진입부를 넣을 수 있어요.
                    </p>
                </div>
            </main>
        </div>
    );
}
