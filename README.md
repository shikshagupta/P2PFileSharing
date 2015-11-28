# P2PFileSharing
This project lets file sharing in  peer to peer manner. 
There is a central server which contains the file and splits the file into chunks (size of chunks is configurable). Peers connect to server to get some chunks from the server and other chunks from their neighbours. 
In this project, each peer is configured to have 1 upload neighbour - to which it uploads the chunks that it has, and one download neighbour from which it downloads the chunks. The neighbours are distributed in a ring fashion.
This project has been configured to have a total of five neighbours.
After all file parts have been received, a peer merges the parts to recreate a duplicate of the original file.
This is an amateur effort for a networks course project.
