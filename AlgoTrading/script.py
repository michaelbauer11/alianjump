import pandas as pd
import matplotlib.pyplot as plt
import datetime as dt
from matplotlib import style
# import pandas_datareader.data as web
from mplfinance.original_flavor import candlestick_ohlc
import mplfinance
#from matplotlib.dates import dates2num

CSV_FILE = 'Tesla.2010-2013.csv'

style.use('ggplot')

#def get_stocks_to_csv()
    #start = dt.datetime(2010 ,1, 1)
    #end = dt.datetime(2013, 1, 1)

    #df = web.DataReader('TSLA', 'yahoo', start, end)
    #df.to_csv('Tesla.2010-2013.csv')


# We Read the csv file and dertermine its first column as the index column
df = pd.read_csv(CSV_FILE, parse_dates=True, index_col=0)


def new_column_manipulation_with_subplots():

    # Create new column - last 100 days close price mean(avarge)
    df['100ma'] = df['Adj Close'].rolling(window=100, min_periods=0).mean()

    # Drop Nan(not a number) rows.
    # Equals to: df = df.dropna()
    df.dropna(inplace=True)

    ax1 = plt.subplot2grid((6,1), (0,0), rowspan=3, colspan=1)
    ax2 = plt.subplot2grid((6,1), (3,0), rowspan=3, colspan=1)
    
    ax1.plot(df.index, df['Adj Close'])
    ax1.plot(df.index, df['100ma'])
    ax2.plot(df.index, df['Volume'])

    plt.show()


def simple_graph_for_column():
    # Create graph for High column
    df[['High']].plot()
    plt.show()


def some_cool_finance_graph():

    # Resample a new pandas csv, that mean(avarage) the data of 10 days into one row. 
    # Than, our df will contains only the 
    # ohlc(Open, Close, Low, High) data.   
    df_ohlc = df['Adj Close'].resample('10D').ohlc()
    df_volume = df['Volume'].resample('10D').sum()

 #   df_ohlc.reset_index(inplace=True)
#    df_ohlc.index.to_datetime(inplace=True)
    
    mplfinance.plot(df, type='candle', title='Tesla, ', style='charles', ylabel='Price ($)')
    return
    

    ax1 = plt.subplot2grid((6,1), (0,0), rowspan=3, colspan=1)
    ax2 = plt.subplot2grid((6,1), (3,0), rowspan=3, colspan=1)
    ax1.xaxis_date()

    # Some cool finance graphs
    candlestick_ohlc(ax1, df_ohlc.values, width=2, colorup='g')
    ax2.fill_between(df_volume.index.map(mdates.date2num), df_volume.values, 0)
    
    plt.show()
some_cool_finance_graph()
