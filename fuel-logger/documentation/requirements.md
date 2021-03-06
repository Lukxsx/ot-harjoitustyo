# Requirements specification

## Purpose
The purpose of this software is to keep logs of refueling costs of a car.
The software can then view graphs and other interesting stats of your car
and it's costs. You can add multiple cars to the software. 

## Users
There will be no other roles than normal users because there is no really
need for them.

## Basic functionality
- [x] Graphical user interface
- [x] Data is stored in SQLite database
- [x] Adding a car (name, fuel tank capacity etc.)
- [x] Selecting a car to use
- [x] Logging a refueling (kilometrage, volume in litres)
- [x] Viewing average fuel consumption
- [x] Viewing graphs of fuel consumption
- [x] Viewing graphs of cost
- [x] Viewing graphs of driven kilometres
- [x] Configuration file to specify database file name

## Future features
- [ ] Text-based UI for quick fuel logging without need for opening the
   graphical UI
- [ ] Logging of other costs like repairs, taxes and insurances
- [ ] Support for other than SI and euro units (miles, dollars etc.)
- [ ] Exporting data as CSV or some other common file type
- [ ] Backup and restore of the data to a file (to use the software easily on
  another computer)

## Limitations
* Only works when tank is filled full, no partial refuelings
* Min. 2 refuelings needed to count fuel consumptions
* Must work on common Linux distributions
* Only supports SI units and euros
* Data is saved locally
