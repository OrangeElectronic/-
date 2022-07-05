import pickle

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait

# options = webdriver.ChromeOptions()

# options.add_experimental_option("debuggerAddress", "127.0.0.1:19222")
# 抓取貼文內容--------------------
# browser = webdriver.Chrome('/Users/jianzhi.wang/Downloads/fi/chromedriver', options=options)
browser = webdriver.Chrome('/Users/orangerd/Desktop/Orange Electronic/Orange_Product/auto test/chromedriver')
# ------ 前往該網址 ------
browser.get('https://www.instagram.com/')
try:
    cookies = pickle.load(open("cookies.pkl", "rb"))
    for cookie in cookies:
        browser.add_cookie(cookie)
except:
    print("An exception occurred")


# ------ 登入函示 ------
def signIn():
    WebDriverWait(browser, 30).until(EC.presence_of_element_located((By.NAME, 'username')))
    # ------ 網頁元素定位 ------
    username_input = browser.find_elements_by_name('username')[0]
    password_input = browser.find_elements_by_name('password')[0]
    print("inputing username and password...")
    # ------ 輸入帳號密碼 ------
    username_input.send_keys("sam38124")
    password_input.send_keys("sam285200")
    # ------ 登入 ------
    WebDriverWait(browser, 30).until(
        EC.presence_of_element_located((By.XPATH, '//*[@id="loginForm"]/div/div[3]/button/div')))
    # ------ 網頁元素定位 ------
    login_click = browser.find_elements_by_xpath('//*[@id="loginForm"]/div/div[3]/button/div')[0]
    # ------ 點擊登入鍵 ------
    login_click.click()


# signIn()
pickle.dump(browser.get_cookies(), open("cookies.pkl", "wb"))
# 想要抓取資料的頁面網址
browser.get('https://www.instagram.com/baden_baby/')
# 要爬取的文章
needScraper = []
for a in browser.find_elements_by_tag_name('a'):
    if "https://www.instagram.com/p/" in a.get_attribute('href'):
        needScraper.append(a.get_attribute('href'))

for b in needScraper:
    browser.get(b)
    if len(browser.find_elements_by_class_name('C4VMK')) > 0:
        print(browser.find_elements_by_class_name('C4VMK')[0].text)

# browser.close()

# 檢查是否有抓到資料
# post_content_element
#
# # 第0項是貼文內容的資料，並利用.text解析出文字
# print(post_content_element[0].text)
