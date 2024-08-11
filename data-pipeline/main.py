from os import path
from pytz import timezone
from datetime import datetime
import pandas as pd
from sklearn.preprocessing import MinMaxScaler

KST = timezone("Asia/Seoul")
today = datetime.now().astimezone(KST)
today_string = "%04d%02d%02d" % (today.year, today.month, today.day)

# 오늘 날짜의 데이터 읽기
if not path.exists(f"data-pipeline/raw-data/{today_string}.csv"):
    raise FileNotFoundError(f"data-pipeline/raw-data/{today_string}.csv")
data = pd.read_csv(f"data-pipeline/raw-data/{today_string}.csv")

# 20시와 05시 사이의 데이터만 필터링
data["operated_at"] = pd.to_datetime(data["operated_at"])
data = data[(data["operated_at"].dt.hour >= 20) | (data["operated_at"].dt.hour <= 5)]

# 'avg_lightlux' 값을 0과 1 사이의 값으로 정규화
scaler = MinMaxScaler()
data["weight"] = scaler.fit_transform(data[["avg_lightlux"]])

# IQR의 1.5배 이상 떨어진 데이터를 이상치로 간주하고 제거
Q1 = data["avg_lightlux"].quantile(0.25)
Q3 = data["avg_lightlux"].quantile(0.75)
IQR = Q3 - Q1

data = data[
    ~(
        (data["avg_lightlux"] < (Q1 - 1.5 * IQR))
        | (data["avg_lightlux"] > (Q3 + 1.5 * IQR))
    )
]

# 위경도를 소수점 셋째 자리까지 반올림하고 위경도가 같은 데이터 중 'avg_lightlux'가 가장 큰 데이터만 남김
data["latitude"] = data["latitude"].apply(lambda x: round(x, 3))
data["longitude"] = data["longitude"].apply(lambda x: round(x, 3))

data = data.loc[data.groupby(["latitude", "longitude"])["avg_lightlux"].idxmax()]

# 상위 N개의 데이터 추출
data = data.nlargest(200, "weight")

data.to_csv(f"data-pipeline/processed-data/{today_string}.csv", index=False)
