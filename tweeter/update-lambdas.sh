aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name RegisterHandler
aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name LoginHandler
aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name GetUserHandler
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name tweeter-getUser > ./log.txt
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name getFeed > ./log.txt
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name tweeter-getStory > ./log.txt
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name tweeter-postStatus > ./log.txt
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name tweeter-getFollowingCount > ./log.txt
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name tweeter-getFollowing > ./log.txt
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name tweeter-getFollowersCount > ./log.txt
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name tweeter-getFollowers > ./log.txt 
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name tweeter-follow > ./log.txt 
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name tweeter-unfollow > ./log.txt 
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name tweeter-getIsFollower > ./log.txt 
