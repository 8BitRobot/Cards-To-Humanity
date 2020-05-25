// Put functions here that access API functionality from the backend.

function apiDecodeJsonResponse(raw_json) {
    // Strip the "for(;;);" from the response (used to block some cross-site hacks) and decode the JSON.
    return JSON.parse(raw_json.substring("for(;;);".length));
}

function apiUserExists(username_or_email) {
    return new Promise((resolve, reject) => {
        axios.get("/user_exists", {
            params: {
                username_or_email: username_or_email
            }
        })
            .then(function(response) {
                var decoded = apiDecodeJsonResponse(response.data);
                resolve(decoded["result"]);
            })
            .catch(function(error) {
                console.log("User exists endpoint error.");
                resolve(false);
            });
    });
}

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

function apiCreateCard(media_id, title, caption) {
    return new Promise((resolve, reject) => {
        const params = new URLSearchParams();
        params.append("media_id", media_id);
        params.append("title", title);
        params.append("caption", caption);

        axios.post("/create_card", params)
            .then(function(response) {
                resolve(parseInt(response.data));
            })
            .catch(function(error) {
                reject(error.response.data);
            });
    });
}
