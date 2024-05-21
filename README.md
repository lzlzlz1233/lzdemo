# lzdemo
## Build tools & versions used
Version: Android Studio Jellyfish | 2023.3.1
Java : 17.0
Build Variant : Debug (Default)
Gradle; 
## Steps to run the app
1) Open Android Studio, build Gradle, select app configuration with emulator Pixel 3a API 34 , then we should be able to run the app by clicking on the round  arrow on the green button
![Screenshot 2024-04-18 at 8 32 22 PM](https://github.com/lzlzlz1233/androidTestLZ/assets/159413314/cb902a5e-e211-4d38-8ce8-96c94a991abf)

## Key UI made Decisions
1. Search will be triggered  when user hit return when they finish typing (see demo)
2. when user refresh all search queries will be reset
3. There is no local persistence of data source given trending movies can change day by day
4.  App Theme toggling is not supported


## Functional Requirements Brief Summary
1. Render a grid of trending movies from the movie database API (poster image only)   ` Approach: See architecture design `

2. Search movies from the movie database API (poster image only)   ` Approach: See architecture design `

4. reload the grid ` Approach: SwipeRefreshLayout `

5. handle the normal variety of errors (empty, and error states)   `Approach: Simple Text View when errored out (see screen shot attached)`

6. If images fail to load/Null , displaying a placeholder  `Approach: Glide`

7. State preservation on orientation change `Approach: StateHandle`
   
8. Overlay   `Approach: Simple Dialogfragment `

9. Pagination (see demo)

10 Unit tests (samples can be find in code)
## Demo

# Trending:

https://github.com/lzlzlz1233/lzdemo/assets/159413314/f9781635-12fc-4b2c-993c-f817265a6a4c

# Search:

https://github.com/lzlzlz1233/lzdemo/assets/159413314/b40d5245-38e2-48cf-9d1b-299ba7fe96f6

# Overlay:


https://github.com/lzlzlz1233/lzdemo/assets/159413314/6d8db628-1efd-44a9-bfde-df329d21545c


# Error Handling
| Empty         |  NetWorkError |
| ------------- | ---------------|
|https://github.com/lzlzlz1233/lzdemo/assets/159413314/f7a4c8cd-96a4-493d-8b74-73c13f44446b| https://github.com/lzlzlz1233/lzdemo/assets/159413314/5488439d-6691-48da-9499-f656d62479cd|




## Architecture Design Graph 
![arch](https://github.com/lzlzlz1233/lzdemo/assets/159413314/7247bbb8-c5bb-4f0f-a0a2-85bbab62846d)

## Area of Focus
1) Achieving a Good Separation of Concerns: How can we achieve a Good separation of concerns ? if we were to develop this app with a team of engs, how can we design the
    app such that each engineer can take on parts as independent as possible without coupling each others code? How can things be tested more easily?

2) Selecting an Architecture for Easy Addition of New Features: What kind of architecture we should use so new features can be added on easily in future iterations

3) How can we leverage state of art libraireis so we can avoid reinventing the wheels?


## Trade Off 
1) Due to the modularization of the project, the codebase inevitably expands with the inclusion of multiple modules, resulting in increased dependencies to maintain than a single module app. However, I believe this approach is adopted to ensure the scalability and adaptability of the app to future requirements.
   
2) In terms of image rendering, the current implementation relies on a simplistic approach utilizing Glide to manage images of varying sizes via hard coding. However, given more time, a refined and sophisticated design and implementation would be pursued to ensure that images are rendered more appropriately, respecting their individual aspect ratios. This enhanced approach would contribute to a more polished user experience and visual presentation.
   
3) Regarding error handling, the current methodology employs a generic approach that only differentiate among basic errors, such as network issues, IO failures. Given additional time, a tailored approach would be undertaken to construct a dedicated exception class, thereby providing more nuanced and specific error handling for different use cases. This revision would enable the creation of more informative and user-friendly interfaces.


## Other things to consider:
i18n, assassbility, user preference for theme (dark/light), auto scroll to top...

## References
references: Android open source github page
https://github.com/android/architecture-samples?tab=readme-ov-file
https://github.com/android/nowinandroid?tab=readme-ov-file
More detailed file referenced are included in the comments in individual files.

 
