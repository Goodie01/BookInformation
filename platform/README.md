# BookInformation
[![pipeline status](https://gitlab.com/Goodie_/bookInformation/badges/master/pipeline.svg?style=flat-square)](https://gitlab.com/Goodie_/BookInformation/)

A service that looks up and combines information from several sources into a easy(ier) to consume format.

An example for calling this API is included below;

    curl --location --request GET 'https://bookinformation.seshat.cc/v1/book?searchType=ISBN&searchTerm=9780785192558' --header 'authorization: testing123'

## Parameters
### searchType
Describes the type of search, currently only supports "ISBN"
### searchTerm
The text for what your searching for, in this case a ISBN number with only numeric characters
### authorization
Authorization token. If you'd like one send me a message, or something.
## Caveats
We will endeavor to use/follow semver where possible. In releasing a new, incompatible, major version we will endeavor to keep the /v1/ version of the API working
