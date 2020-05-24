const app = new Vue(
    {
        el: "#vue_div",
        data: {
                title: "",
                caption: "",
                error_message: "",
                uploaded_image_url: ""
        },
        methods: {
                uploadMediaFile: function() {
                        var self = this;

                        apiUploadMedia(this.$refs.file_upload)
                            .then(function(media_id) {
                                console.log(`Media upload successful, returned media_id ${media_id}.`);
                                self.uploaded_image_url = `/get_media?media_id=${media_id}`;
                            })
                            .catch(function(error) {
                                self.error_message = error;
                            });
                }
        }
    }
);