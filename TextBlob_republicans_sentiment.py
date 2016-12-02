
from textblob.sentiments import NaiveBayesAnalyzer
from textblob.classifiers import NaiveBayesClassifier
from textblob import TextBlob
import random
import csv
import unicodedata

cr = csv.reader(open("republicansTweetsOnly.csv","rb"))
for row in cr:    
    message = row[0]
    text = unicodedata.normalize('NFKD',message.decode("utf=8")).encode('ascii','ignore')
    blob = TextBlob(text)
    with open('democrats_Sentiments_Only_total.csv', 'a') as f:
        a = (blob.sentiment)
        writer = csv.writer(f)
        writer.writerow(a)
print("Task Completed")
