aws lambda invoke --function-name RegisterHandler --payload '{}' response.json
echo "1/16 done"
aws lambda invoke --function-name LoginHandler --payload '{}' response.json
echo "2/16 done"
aws lambda invoke --function-name LogoutHandler --payload '{}' response.json
echo "3/16 done"
aws lambda invoke --function-name GetUserHandler --payload '{}' response.json
echo "4/16 done"
aws lambda invoke --function-name GetFollowingCountHandler --payload '{}' response.json
echo "5/16 done"
aws lambda invoke --function-name GetFollowersCountHandler --payload '{}' response.json
echo "6/16 done"
aws lambda invoke --function-name IsFollowerHandler --payload '{}' response.json
echo "7/16 done"
aws lambda invoke --function-name FollowHandler --payload '{}' response.json
echo "8/16 done"
aws lambda invoke --function-name UnfollowHandler --payload '{}' response.json
echo "9/16 done"
aws lambda invoke --function-name GetFollowingHandler --payload '{}' response.json
echo "10/16 done"
aws lambda invoke --function-name GetFollowersHandler --payload '{}' response.json
echo "11/16 done"
aws lambda invoke --function-name PostStatusHandler --payload '{}' response.json
echo "12/16 done"
aws lambda invoke --function-name GetFeedHandler --payload '{}' response.json
echo "13/16 done"
aws lambda invoke --function-name GetStoryHandler --payload '{}' response.json
echo "14/16 done"
aws lambda invoke --function-name PostUpdateFeedMessages --payload '{}' response.json
echo "15/16 done"
aws lambda invoke --function-name UpdateFeeds --payload '{}' response.json
echo "16/16 done"
echo "All lambdas awake"