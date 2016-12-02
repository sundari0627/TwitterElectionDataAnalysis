import time
import tweepy
import csv

#Twitter API credentials
consumer_key = "iloGKluD4VQr2jmDf5OfkIaaw"
consumer_secret = "EvEuyrGKHHptkis9KKUpyqBNhFyg2d5sbhUFzd0h9Ztbw0iHDJ"
access_key = "136884740-GBo3L4tRNamUGlm8HSLQKKnQku0Yjrq43eCEPOlA"
access_secret = "4GHaef21QHnxlD2aR6drKPjas40VB3hz3tQoUUdTLLAkV"

auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_key, access_secret)

api = tweepy.API(auth,wait_on_rate_limit=True)

ids = []
for page in tweepy.Cursor(api.followers_ids, screen_name="@amyklobuchar").pages():
    ids.extend(page)
    time.sleep(60)
    with open('amyklobuchar_followerIds.csv', 'wb') as f:
                writer = csv.writer(f)
                writer.writerows([ids])


print len(ids)
