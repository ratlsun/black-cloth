FTP_URL=ftp://123.56.130.54

echo "curl -u $FTP_USERNAME:FTP_PASSWORD -T bc-ss/target/black-cloth-0.0.1.jar $FTP_URL/"
curl -u $FTP_USERNAME:FTP_PASSWORD -T bc-ss/target/black-cloth-0.0.1.jar $FTP_URL/