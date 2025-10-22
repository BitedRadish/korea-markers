import SignIn from "@pages/auth/SignIn";
import SignUp from "@pages/auth/SignUp";
import MainPage from "@pages/MainPage";
import { createBrowserRouter } from "react-router-dom";

export const Router = createBrowserRouter([
    { path: "/main", element: <MainPage /> },
    { path: "/signin", element: <SignIn /> },
    { path: "/signup", element: <SignUp /> },
]);
