// src/pages/LoginPage.tsx
import { Link, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { loginSchema } from "@shared/schema/SignInSchema";
import type { LoginFormValues } from "@shared/schema/SignInSchema";
import { postSignIn } from "@shared/api/auth";

export default function SignIn() {
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm<LoginFormValues>({
        resolver: zodResolver(loginSchema),
        mode: "onChange",
    });

    const onSubmit = async (values: LoginFormValues) => {
        const res = await postSignIn(values);
        console.log(res);

        if (res) {
            localStorage.setItem("accessToken", res.data.accessToken);
            alert("로그인이 성공하였습니다.");
            navigate("/main");
        } else {
            alert("올바르지 않은 비밀번호거나 없는 아이디입니다.");
        }
    };

    return (
        <div className="container py-5" style={{ maxWidth: 420 }}>
            <h1 className="text-center mb-4">로그인</h1>

            <div className="card shadow-sm">
                <div className="card-body">
                    <form onSubmit={handleSubmit(onSubmit)} noValidate>
                        <div className="mb-3">
                            <label className="form-label">이메일</label>
                            <input
                                type="email"
                                className={`form-control ${
                                    errors.email ? "is-invalid" : ""
                                }`}
                                placeholder="you@example.com"
                                {...register("email")}
                            />
                            {errors.email && (
                                <div className="invalid-feedback">
                                    {errors.email.message}
                                </div>
                            )}
                        </div>

                        <div className="mb-3">
                            <label className="form-label">비밀번호</label>
                            <input
                                type="password"
                                className={`form-control ${
                                    errors.password ? "is-invalid" : ""
                                }`}
                                {...register("password")}
                            />
                            {errors.password && (
                                <div className="invalid-feedback">
                                    {errors.password.message}
                                </div>
                            )}
                        </div>

                        <button
                            type="submit"
                            className="btn btn-primary w-100"
                            disabled={isSubmitting}
                        >
                            {isSubmitting ? "로그인 중..." : "로그인"}
                        </button>
                    </form>

                    <div className="text-center mt-3">
                        <span className="text-muted">계정이 없으신가요? </span>
                        <Link to="/signup">회원가입</Link>
                    </div>
                </div>
            </div>
        </div>
    );
}
