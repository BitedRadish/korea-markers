import { createRoot } from "react-dom/client";
import "bootstrap/dist/css/bootstrap.min.css";
import { AppRouterProvider } from "@app/provider/AppRouterProvider";

createRoot(document.getElementById("root")!).render(<AppRouterProvider />);
