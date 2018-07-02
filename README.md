# SchematicViewer
Browse schematics though a clean and simple GUI.

### Features
- Simple GUI to view schematics
- Custom sort options
- Render any schematic
- Supports folders for organization
- Folder permissions
- Open source!
- Requires WorldEdit

### Commands
`/<schematic|sch|sv>` - Brings up default directory
`/sv page <number>` - Display the page of your last search
`/sv [parameters]` - Searches based on parameters provided

####Parameters
`path=<directory>` - Directory to load relative to the WorldEdit schematic directory (default: /, alias: p)
`compare=<comparetype>` - Sort results by this type (default: name, alias: c)
`foldersfirst=<boolean>` - Place folders first in the results (default: true, alias: ff)

####Compare Types
`NAME` - sorts in alphabetical order

####Support & Bugs
- Paramters cannot contain spaces
- Java 8+ (uses lamada expressions)

This plugin has been tested on 1.12 only. Theoretically, this plugin should work on all versions of Minecraft 1.9 and later.