import { RouterProvider } from "react-router";
import { Router } from "@app/router/Router";

export const AppRouterProvider = () => {
    return <RouterProvider router={Router} />;
};
