# Search_Engine

The project is Simple Search Engine using java which is dedicated for Computer Science. It searches the web and selects the links 
then proceeds with that links for further process like Ranking. This search engine will use crawl web,indexing, graph and 
ranking mechanisms to give the better results to user. Depth Search Algorithm is used to collect the chunks which will 
sorted later to obtain the results. Hence it gives the result regarding the search request.The result will be only related to 
Computer Science.If the keyword(Search query) is not related to computer science result will be produced as a suggestion.


MODULES

Single Process

a) Profiling the Index, Graph and Rank
b) Removing Common words from index
c) Validation Test
d) Use Depth Search Mechanism
e) Removing Unnecessary HTML Tags
f) Combining the search of two or three keywords
g) Producing results.

Parallel Process

Parallelize the Crawl

a) Extract the no of links from single seed page and chunk the links for crawling process
b) Chunk the list of seed_ links and assign to each process for crawling.
c) Consolidate the crawl results from all process.
d) Use Multiple Seed links to crawl and tag the crawling details.
e) In Single Process Search Engine, modify the crawl web to get multiple seed page / multiple links to crawl
f) Get the seed page/ multiple seed links from the user interactively using command line inputs.

Parallelize the Ranking

a) Chunk the graph and assign it into each process to compute rank for the web pages.
b) Consolidate the rank results.

Distributed Process of Search Engine (Hadoop)

a) Understand Map/Reduce Concepts
b) Write mapper_search and reducer_search code
c) Try it on Single-node; Compare the performance with Single/Normal version of search engine.
d) Distribute the search process through single-node machine, calculate the performance.

Note: Hadoop implementation is added separately in another repositary(because of configuration).
  Link: https://github.com/javasearchengine/Hadoop_Implementation.git


