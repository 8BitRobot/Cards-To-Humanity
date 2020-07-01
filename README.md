# Cards To Humanity
A website collecting wholesome user-submitted cards and spreading positivity :)  
Find us at cardstohumanity.org

## Development Team

* Oliver Trevor - Backend (Java + SQL)

* Premkumar Giridhar - Frontend (JavaScript + HTML + CSS + JS)

* Will Liang - Machine Learning (Python w/ TensorFlow)

## Running
The following environment variables must be set (preferably as Heroku parameters) for the software to run correctly:

* `JAWSDB_MARIA_URL` - The URL of the MariaDB database provided by the JawsDB Maria addon for Heroku. If this environment variable is NOT set, then the program will default to a locally-hosted MariaDB database on port 3306 with username `root` and password `none`.

* `S3_BUCKET_NAME` - The name of the Amazon AWS S3 bucket to store image data in. The bucket must be located in the `US_WEST_1` region. NEVER use a production bucket for testing. Always have one bucket for production and one for testing.

* `AWS_ACCESS_KEY_ID` - AWS's API key. Found in the CSV file that AWS lets you download when you create a new IAM user.

* `AWS_SECRET_ACCESS_KEY` - AWS's secret API key. Found in the CSV file that AWS lets you download when you create a new IAM user.
