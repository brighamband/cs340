aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name RegisterHandler
aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name LoginHandler
aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name LogoutHandler
aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name GetUserHandler
aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name GetFollowingCountHandler
aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name GetFollowersCountHandler


# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name GetStoryHandler
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name GetFeedHandler
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name GetFollowingHandler
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name GetFollowersHandler
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name FollowHandler
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name UnfollowHandler
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name IsFollowerHandler
# aws lambda update-function-code --zip-file fileb://server/build/libs/server-all.jar --function-name PostStatusHandler
