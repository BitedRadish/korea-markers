import axios from "axios";

export const apiInstance = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
    withCredentials: true,
});

apiInstance.interceptors.request.use(
    (config) => {
        if (
            config.url === "/api/auth/signin" ||
            config.url === "/api/auth/signup"
        ) {
            return config;
        }

        config.headers.Authorization = `Bearer ${localStorage.getItem(
            "accessToken"
        )}`;

        return config;
    },
    (error) => Promise.reject(error)
);

// // 401 시 자동 갱신
// apiInstance.interceptors.response.use(
//     (response) => response,
//     async (error) => {
//         const originalRequest = error.config;

//         if (error.response?.status === 401 && !originalRequest._retry) {
//             originalRequest._retry = true;

//             try {
//                 const response = await axios.post(
//                     `${import.meta.env.VITE_API_URL}/api/auth/refresh`,
//                     {},
//                     { withCredentials: true }
//                 );

//                 console.log(response);

//                 const { accessToken: newAccessToken } =
//                     response.data.accessToken;
//                 setAccessToken(newAccessToken);

//                 originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
//                 return apiInstance(originalRequest);
//             } catch (refreshError) {
//                 clearAccessToken();
//                 window.location.href = "/login";
//                 return Promise.reject(refreshError);
//             }
//         }

//         return Promise.reject(error);
//     }
// );
