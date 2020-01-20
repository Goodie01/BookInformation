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

## API response
The API response will look something like the below. Please keep in mind that we prefer to leave a blank (string) field as opposed to a null or missing field.

For our definition of "Backwards compatible" we may choose to add fields, but we will not remove fields.

        {
            "infoList": [
                {
                    "title": {
                        "seriesTitle": "",
                        "title": "Star Wars: Darth Vader, Vol. 1: Vader",
                        "subTitle": ""
                    },
                    "authors": [
                        {
                            "name": "Kaare Kyle Andrews",
                            "description": "Illustrator"
                        },
                        {
                            "name": "Leinil Francis Yu",
                            "description": "Illustrator"
                        },
                        {
                            "name": "Juan Giménez",
                            "description": "Illustrator"
                        },
                        {
                            "name": "Adi Granov",
                            "description": "Illustrator"
                        },
                        {
                            "name": "Kieron Gillen",
                            "description": ""
                        },
                        {
                            "name": "Salvador Larroca",
                            "description": "Illustrations"
                        }
                    ],
                    "description": "The original Dark Lord of the Sith stars in his first ongoing series! Ever since Darth Vader's first on-screen appearance, he has become one of pop-culture's most popular villains. Now, follow Vader straight from the ending of A NEW HOPE (and the pages of the new STAR WARS comic book) into his own solo adventures — showing the Empire's war with the Rebel Alliance from the other side! But when a Dark Lord needs help, who can he turn to? As Vader pursues a very personal vengeance against the Rebels and investigates the Emperor's secret machinations, he clashes with weapons scavenger Aphra and deadly Battle Droids, and returns to Geonosis to build an army. But some very powerful people don't want him to learn the truths he seeks! Guest-starring Jabba the Hutt, Boba Fett and more! <br /><br />Collecting DARTH VADER #1-6.",
                    "isbn10": {
                        "value": "0785192557"
                    },
                    "isbn13": {
                        "value": "9780785192558"
                    },
                    "image": {
                        "value": "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1525359589l/24819813.jpg"
                    },
                    "source": "GOOD_READS"
                }
            ]
        }