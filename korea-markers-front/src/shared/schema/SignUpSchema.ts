import { z } from "zod";

export const signupSchema = z
    .object({
        email: z.string().email("올바른 이메일 주소를 입력해주세요"),
        password: z.string().min(8, "비밀번호는 최소 8자 이상이어야 합니다"),
        passwordCheck: z.string().min(8, "비밀번호 확인을 입력해주세요"),
    })
    .refine((v) => v.password === v.passwordCheck, {
        message: "비밀번호가 일치하지 않습니다",
        path: ["passwordCheck"],
    });

export type SignupFormValues = z.infer<typeof signupSchema>;
