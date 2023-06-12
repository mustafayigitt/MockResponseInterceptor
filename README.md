# MockResponseInterceptor
MockResponseInterceptor - Request from Local Jsons without any changes. Dynamic configuration change support!

[![mustafayigitt - mockresponseinterceptor](https://img.shields.io/static/v1?label=mustafayigitt&message=mockresponseinterceptor&color=blue&logo=github)](https://github.com/mustafayigitt/mockresponseinterceptor "Go to GitHub repo")

[![stars - mockresponseinterceptor](https://img.shields.io/github/stars/mustafayigitt/mockresponseinterceptor?style=social)](https://github.com/mustafayigitt/mockresponseinterceptor)
[![forks - mockresponseinterceptor](https://img.shields.io/github/forks/mustafayigitt/mockresponseinterceptor?style=social)](https://github.com/mustafayigitt/mockresponseinterceptor)
[![issues - mockresponseinterceptor](https://img.shields.io/github/issues/mustafayigitt/mockresponseinterceptor)](https://github.com/mustafayigitt/mockresponseinterceptor/issues)

[![GitHub release](https://img.shields.io/github/release/mustafayigitt/mockresponseinterceptor?include_prereleases=&sort=semver&color=blue)](https://github.com/mustafayigitt/mockresponseinterceptor/releases/)
[![License](https://img.shields.io/badge/License-Apache-blue)](#license)

<p align=center>
<img src="https://www.linkpicture.com/q/Frame-1_17.png">
</p>

With MockResponseInterceptor, fetch data from local json files by retrofit endpoints without changes.

## Features
  - Easy use with annotation
  - Test your app locally
  - Change global mocking **dynamically**
  - Customize filename extractor
  
## Dependencies
Add the Jitpack source to project:
```kotlin
  ...
  repositories {
    maven { url 'https://jitpack.io' } 
  }
```

Add the following dependency to your build.gradle(module) file:
```kotlin
  dependencies {
    ...
    implementation 'com.github.mustafayigitt:MockResponseInterceptor:1.0.0'
  }
```

## Init
```kotlin
MockResponseInterceptor.Builder(context.assets).build()

// or

MockResponseInterceptor.Builder(context.assets)
    .isGlobalMockingEnabled { MainActivity.isGlobalMockingEnabled }
    .fileNameExtractor { url -> "yourNamingStrategy" }
    .build()
```

## Usage
### Define your endpoint
```kotlin
@GET("top-headlines")
@Mock
suspend fun getNews(
    @Query("language") country: String = "en",
    @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
): Response<NewsWrapperModel>
```

### Create Mock Json File
```json
{
  "articles": [
    {
      "title": "MockResponseInterceptor - Mock your Retrofit API responses",
      "urlToImage": "https://picsum.photos/200"
     }
  ]
}
```

## Contribution
We welcome all contributions to **mockresponseinterceptor**! 
Please open issue when detect a problem. Every issue will be open to review and merge with pull requests. 

License
=======
    Copyright 2023 @mustafayigitt

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
