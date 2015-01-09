package data.crawler

object Boot extends App
  with PostgresMigrations {

  println("Crawler Started")
  runMigrations()

}

// == crawler ==
// table of "crawlable conical urls"
// - needs a seed list to start with, google news, archive.org
// join ^ with recently crawled sites to generate list of "next crawls"
// send ^ off to be crawled within this app
// - needs rate limiting per host
// todo: some distributed lock? reserve in psql table?
// on response:
// - encrypt and store in s3, postgres
// - send html ids from ^ to extractor
// - compress html as well

// == extractor ==
// pull out urls from html, make conical urls and insert new ones into "to crawl" table
// - ^ urls need a last_crawled_at which determines when to crawl again
