cmake_minimum_required(VERSION 3.22.1)

project("mylib")

add_library(
        mylib
        SHARED
        main_native.c)
find_library(
        log-lib
        log)
target_link_libraries(
        mylib
        ${log-lib})