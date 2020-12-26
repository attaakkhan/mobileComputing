import pandas as pd
import sqlite3
import datetime

# Read sqlite query results into a pandas DataFrame
columns=['_node_id','_latitude','_longitude','_accuracy','_time_location','_stay_time','_stay_start_time','_time_stay']


def timeRmday(str1):

    if(str(str1)== 'nan'):
        return str1

    str1 = str1[:-3]


    return str1

def getJoin(df1,dfs1):
    df_left = pd.merge(df1, dfs1, on='_node_id', how='left')
    #df1 = df_left[(df_left['_time_location'] == df_left['_time_stay']) | (df_left._stay_time.isnull())]
    df1=df_left[columns]
    return df1

def getJoinOnNOde(df1,dfs1):
    df_left = pd.merge(df1, dfs1, on='_jn', how='inner')
    # df1 = df_left[(df_left['_time_location'] == df_left['_time_stay']) | (df_left._stay_time.isnull())]
   # df1=df1[columns]
    return df_left

from datetime import timedelta
timeT=1000
def inBetween(t1,t2,t3):
    if(t3>=t1 and t3 <= t2):
        return True
    return False
def isTimeCollide(row):
       # print(row)

        x_start=str(row['_stay_start_time_x'])
        if(not x_start=='nan'):
            x_start= datetime.datetime.strptime(x_start, '%Y%m%d%H%M%S')

        y_start = str(row['_stay_start_time_y'])
        if (not y_start == 'nan'):
            y_start = datetime.datetime.strptime(y_start, '%Y%m%d%H%M%S')
        x_end = str(row['_time_location_x'])
        # if (x_end == 'nan'):
        #          return False
        x_end = datetime.datetime.strptime(x_end, '%Y%m%d%H%M%S')
        y_end = str(row['_time_location_y'])
        # if (y_end == 'nan'):
        #     return False
        y_end = datetime.datetime.strptime(y_end, '%Y%m%d%H%M%S')


        if (y_start == 'nan'):
            y_start=y_end-timedelta(seconds=timeT)

        if (x_start == 'nan'):
            x_start = x_end - timedelta(seconds=timeT)


        if(inBetween(x_start, x_end,y_start) or inBetween(x_start, x_end,y_end) ):
            return True
        return False
import  math
def haversine(row):
    """
    Calculate distance between two points on earth in km
    See: http://www.movable-type.co.uk/scripts/latlong.html
    :param p1 point 1 tuple (latitude, longitude)
    :param p2 point 2 tuple (latitude, longitude)
    :return distance between points p1 and p2 on earth in km

    [row['_latitude_x'], row['_longitude_x']], [row['_latitude_y'], row['_longitude_y']]
    """
    p1=[row['_latitude_x'], row['_longitude_x']]
    p2=[row['_latitude_y'], row['_longitude_y']]
    R = 6371     # earth radius in km
    p1 = [math.radians(v) for v in p1]
    p2 = [math.radians(v) for v in p2]

    d_lat = p2[0] - p1[0]
    d_lng = p2[1] - p1[1]
    a = math.pow(math.sin(d_lat / 2), 2) + math.cos(p1[0]) * math.cos(p2[0]) * math.pow(math.sin(d_lng / 2), 2)
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))

    return R * c*1000

def correctEndTime(row):
    start = str(row['_stay_start_time'])
    end = str(row['_time_stay'])
    endOrig = str(row['_time_location'])
    if(end=='nan'):
        return row['_time_location']
    if (end == endOrig):
        return row['_time_location']
    else:
        return  row['_time_stay']





def timeRmdayJoin(str1):

    if(str(str1)== 'nan'):
        return str1

    str1 = str1[:-5]


    return str1


date=0

def past7(row):
    #print(row)

    start = str(row['_stay_start_time'])
    end = str(row['_time_location'])

    date1 = datetime.datetime.strptime(str(date), '%Y%m%d%H%M%S')
    end = datetime.datetime.strptime(str(end), '%Y%m%d%H%M%S')
    #print(date1)
    date7=date1-timedelta(days=7)
    #print(date7)

    if(inBetween(date7,date1, end)):
        return True


    if(start=='nan'):
        return False


    start = datetime.datetime.strptime(str(start), '%Y%m%d%H%M%S')
    if(inBetween(date1, date7,start)):
        return True


    return False







#        print(str(x_start))



def findcontact(df1, dfs1, df2=None, dfs2=None, datet=None):
           # print('AAAAA')
            #data=20110308115716
            global date
            date=datet
           # print(date)
            df1=getJoin(df1,dfs1)

            df1['_time_location'] = df1.apply(correctEndTime, axis=1)
            df2 = getJoin(df2, dfs2)
            df2['_time_location'] = df2.apply(correctEndTime, axis=1)

            df1['_time_location'] = df1['_time_location'].apply(timeRmday).copy()
            df1['_jn'] = df1['_time_location'].apply(timeRmdayJoin).copy()

            df1['_stay_start_time'] = df1['_stay_start_time'].apply(timeRmday).copy()
            df2['_time_location'] = df2['_time_location'].apply(timeRmday).copy()
            df2['_jn'] = df2['_time_location'].apply(timeRmdayJoin).copy()
            df2['_stay_start_time'] = df2['_stay_start_time'].apply(timeRmday).copy()

            df1['past'] = df1.apply(past7,axis=1).copy()
            df1 = df1[df1['past'] ==True]
            df2['past'] = df2.apply(past7,axis=1).copy()
            df2 = df2[df2['past'] == True]
            df1=df1[['_latitude','_longitude','_time_location','_stay_start_time','_jn']]
            df2 = df2[['_latitude', '_longitude', '_time_location', '_stay_start_time', '_jn']]



            #print (df1)
            dfr=getJoinOnNOde(df1,df2)
            if(dfr.shape[0]==0):
                return list([])


            dfr['collide'] =  dfr.apply(isTimeCollide, axis=1).copy()
            dfr=dfr[dfr.collide == True]
            dfr = dfr[['_latitude_x','_longitude_x','_latitude_y','_longitude_y','_time_location_y']]
            dfr['distance'] = dfr.apply(haversine,axis=1)
            dfr=dfr[dfr['_latitude_x'] > 0]
            dfr = dfr[dfr['distance']  <500]
            # print(dfr)
            return list(dfr['_time_location_y'])






import sys

person=int(sys.argv[1])
dated=int(sys.argv[2])
# 20110620115716

matrix=[[0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0]]



def rec(person, dated, remlist=[]):


        listt=[1,2,3,4,5,6,7,8,9,10,11]

        listt=list(set(listt)-set([person])-set(remlist))

        for i in listt:

              con = sqlite3.connect(sys.argv[3]+"/python/LifeMap_GS"+str(person)+".db")
              df1 = pd.read_sql_query("SELECT * from locationTable order by _node_id desc", con)
              dfs1 = pd.read_sql_query("SELECT * from stayTable order by _node_id desc", con)
              con.close()
              con = sqlite3.connect(sys.argv[3]+"/python/LifeMap_GS"+str(i)+".db")
              df2 = pd.read_sql_query("SELECT * from locationTable order by _node_id desc", con)
              dfs2 = pd.read_sql_query("SELECT * from stayTable order by _node_id desc", con)
              con.close()
              contact=findcontact(df1, dfs1, df2, dfs2,dated)
              if(len(contact)>0):
                  matrix[person-1][i-1]=1
                 # print ("person="+str(person)+" Contact="+str(i))
                  for c in contact:
                      remlist=remlist.copy()
                      remlist.append(person)
                      rec(i,c,remlist)








# print('AAAAA')
# print('AAAAA222222')
rec(person,dated,[])
for i in range(11):
    if(i<9):
         print(" "+str(i+1)+" "+str(matrix[i]))
    else:
         print(""+str(i+1)+" "+str(matrix[i]))

# print(matrix)
#

# for i in range(11):
#     if(i+1==person):
#         continue
#
#     print("i="+str(i+1)+" p="+str(person))
#
#     con = sqlite3.connect("LifeMap_GS"+str(person)+".db")
#     df1 = pd.read_sql_query("SELECT * from locationTable order by _node_id desc", con)
#     dfs1 = pd.read_sql_query("SELECT * from stayTable order by _node_id desc", con)
#     con.close()
#     con = sqlite3.connect("LifeMap_GS"+str(i+1)+".db")
#     df2 = pd.read_sql_query("SELECT * from locationTable order by _node_id desc", con)
#     dfs2 = pd.read_sql_query("SELECT * from stayTable order by _node_id desc", con)
#     con.close()
#     contact=findcontact(df1, dfs1, df2, dfs2,dated)
#     if(len(contact)>0):
#         matrix[person][i]=1
#
#         for datec in contact:
#
#
#
#     if(contact==True):
#             print(contact)
# con = sqlite3.connect("LifeMap_GS3.db")
# df3 = pd.read_sql_query("SELECT * from locationTable", con)
# con.close()
# con = sqlite3.connect("LifeMap_GS4.db")
# df4 = pd.read_sql_query("SELECT * from locationTable", con)
# con.close()
# con = sqlite3.connect("LifeMap_GS5.db")
# df5 = pd.read_sql_query("SELECT * from locationTable", con)
# con.close()
# con = sqlite3.connect("LifeMap_GS6.db")
# df6 = pd.read_sql_query("SELECT * from locationTable", con)
# con.close()
# con = sqlite3.connect("LifeMap_GS7.db")
# df7 = pd.read_sql_query("SELECT * from locationTable", con)
# con.close()
# con = sqlite3.connect("LifeMap_GS8.db")
# df8 = pd.read_sql_query("SELECT * from locationTable", con)
# con.close()
# con = sqlite3.connect("LifeMap_GS9.db")
# df9 = pd.read_sql_query("SELECT * from locationTable", con)
# con.close()
# con = sqlite3.connect("LifeMap_GS10.db")
# df10 = pd.read_sql_query("SELECT * from locationTable", con)
# con.close()
# con = sqlite3.connect("LifeMap_GS11.db")
# df11 = pd.read_sql_query("SELECT * from locationTable", con)
# con.close()
# con = sqlite3.connect("LifeMap_GS12.db")
# df12 = pd.read_sql_query("SELECT * from locationTable", con)
# con.close()







#findcontact(df1, dfs1,df2, dfs2)
exit(0)





print(df1[df1['_node_id']==303])
print(dfs1[dfs1['_node_id'] ==303])

df_left = pd.merge(df1, dfs1, on='_node_id', how='left')
exit(0)
accuracy=500
columns=['_latitude','_longitude','_accuracy','_time_location']
df1=df1[df1['_accuracy']<accuracy]
df1=df1[columns]
accuracy=500
df2=df2[df2['_accuracy']<accuracy]
df2=df2[columns]
print(df1)