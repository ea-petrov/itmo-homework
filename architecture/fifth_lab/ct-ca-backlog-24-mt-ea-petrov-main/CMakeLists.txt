cmake_minimum_required(VERSION 3.16)
project(untitled)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fopenmp")

find_package(OpenMP REQUIRED)

add_executable(untitled
        hit.cpp
        main.cpp
)

if(OpenMP_CXX_FOUND)
    target_link_libraries(untitled PRIVATE OpenMP::OpenMP_CXX)
endif()
