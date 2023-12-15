cmake_minimum_required(VERSION 3.4.1)
#创建库
add_library(startup-optimize SHARED  StartUpOptimize.cpp enhanced_dlfcn.c )
# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)
#把库和cmake链接
target_link_libraries(startup-optimize ${log-lib})