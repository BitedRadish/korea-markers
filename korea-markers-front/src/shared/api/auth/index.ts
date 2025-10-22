import { apiInstance } from "@shared/api";
import type { SignupFormValues } from "@shared/schema/SignUpSchema";
import type { LoginFormValues } from "@shared/schema/SignInSchema";

const baseUrl = "/api/auth";

export const postSignIn = async (data: LoginFormValues) => {
    try {
        const res = await apiInstance.post(`${baseUrl}/signin`, data);
        return res.data; // 성공 시 accessToken 포함한 전체 데이터 반환
    } catch (error) {
        return false; // 실패 시 null 반환
    }
};

export const postSignUp = async (data: SignupFormValues) => {
    const res = await apiInstance.post(`${baseUrl}/signup`, data);
    return res.data.success;
};

export const postLogout = async () => {
    try {
        const res = await apiInstance.post(`${baseUrl}/signout`);
        localStorage.removeItem("accessToken");

        return res.status === 204;
    } catch (err) {
        throw new Error("로그아웃 실패");
    }
};
