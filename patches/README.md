# Generating patches

To generate patches, place the new BTW repository zip in a folder named `/src` and run `gradlew ingest`.

## Layout of the zip file

- A folder named `minecraft` and a folder named `minecraft_server` containing the source.
- A folder named `resources` with the BTW textures and resources.
- A copy of the changelog.txt file with the new changes added.
