// src/pages/SignupPage.tsx
import { Link, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { signupSchema } from "@shared/schema/SignUpSchema";
import type { SignupFormValues } from "@shared/schema/SignUpSchema";
import { zodResolver } from "@hookform/resolvers/zod";
import { postSignUp } from "@shared/api/auth";

export default function SignUp() {
    const navigate = useNavigate();

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm<SignupFormValues>({
        resolver: zodResolver(signupSchema),
        mode: "onChange",
    });

    const onSubmit = async (values: SignupFormValues) => {
        const res = await postSignUp(values);
        if (res) {
            alert("회원 가입이 완료되었습니다.");
            navigate("/signin");
        } else {
            alert("중복된 계정 이름입니다.");
        }
    };

    return (
        <div className="container py-5" style={{ maxWidth: 480 }}>
            <h1 className="text-center mb-4">회원가입</h1>

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
                            <div className="form-text">8자 이상 권장</div>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">비밀번호 확인</label>
                            <input
                                type="password"
                                className={`form-control ${
                                    errors.passwordCheck ? "is-invalid" : ""
                                }`}
                                {...register("passwordCheck")}
                            />
                            {errors.passwordCheck && (
                                <div className="invalid-feedback">
                                    {errors.passwordCheck.message}
                                </div>
                            )}
                        </div>

                        <button
                            type="submit"
                            className="btn btn-primary w-100"
                            disabled={isSubmitting}
                        >
                            {isSubmitting ? "가입 중..." : "회원가입"}
                        </button>
                    </form>

                    <div className="text-center mt-3">
                        <span className="text-muted">
                            이미 계정이 있으신가요?{" "}
                        </span>
                        <Link to="/signin">로그인</Link>
                    </div>
                </div>
            </div>
        </div>
    );
}
