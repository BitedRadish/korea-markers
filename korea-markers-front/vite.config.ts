import { defineConfig } from "vite";
import { fileURLToPath } from "url";
import path from "path";
import react from "@vitejs/plugin-react-swc";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    resolve: {
        alias: [
            { find: "@", replacement: path.resolve(__dirname, "src") },
            {
                find: "@app",
                replacement: path.resolve(__dirname, "src/app"),
            },
            {
                find: "@pages",
                replacement: path.resolve(__dirname, "src/pages"),
            },
            {
                find: "@shared",
                replacement: path.resolve(__dirname, "src/shared"),
            },
        ],
    },
});
