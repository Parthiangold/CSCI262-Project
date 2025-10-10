 password generation and how it can be done in Java. There’s a module in the Java library called SecureRandom which is able to do this .

As for the Argon2id hashing algorithm we can use maven to import the module from their library. We can use the Argon2.verify() to check if any of the stored hashes match the generated password.

With the Gson module can be used as a serialiser /deserialiser  to make string parsing easy. We can encrypt and decrypt the stored passwords using AES-GCM.

We would have 2 files a history.txt file which has a history of all previously generated passwords (Argon automatically generates a  random salt that’s encoded within the hash whenever a new password is created so to get a master key we need a salt.bin file to store it for a decryption key)  and  a history.txt file which stores the hashes for each password generated.
