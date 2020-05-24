// Put functions here that access API functionality from the backend.

function apiLoginUser(username_or_email, password) {
    return new Promise((resolve, reject) => {
            const params = new URLSearchParams();
            params.append("username_or_email", username_or_email);
            params.append("password", password);

            axios.post("/login_user", params)
                .then(function (response) {
                    resolve("Login successful.");
                })
                .catch(function (error) {
                    reject("Login failed. Please try again.");
                });
        }
    );
}

function apiCreateUser(username, display_name, password, email) {
    return new Promise((resolve, reject) => {
            const params = new URLSearchParams();
            params.append("username", username);
            params.append("display_name", display_name);
            params.append("password", password);
            params.append("email", email);

            axios.post("/create_user", params)
                .then(function(response) {
                    resolve("User created successfully.");
                })
                .catch(function(error) {
                    console.table(error);
                    reject(error.response.data);
                });
        }
    );
}

function apiUploadMedia(file_upload_input) {
    return new Promise((resolve, reject) => {
        const params = new FormData();
        params.append("media_file", file_upload_input.files[0]);

        axios.post("/upload_media", params, {headers: {"content-type": "multipart/form-data"}})
            .then(function(response) {
                resolve(parseInt(response.data));
            })
            .catch(function(error) {
                reject(error.response.data);
            });
    });
}