# RideSync – Database

This folder contains the complete MySQL database schema for RideSync.

## What's inside

- `ridesync_database.sql` — full schema (6 tables), sample data, and example
  queries (SELECT/JOIN/UPDATE/DELETE).

## Tables

| Table | Purpose |
|---|---|
| `users` | Registered students/staff who can drive or ride |
| `vehicles` | Vehicles owned by users, used when posting rides |
| `rides` | Ride offers posted by drivers |
| `ride_requests` | Passenger requests to join a ride |
| `payments` | Optional fare payment records |
| `reviews` | Post-ride ratings between drivers and passengers |

## How to run it

1. Install MySQL (8.0+) and open MySQL Workbench.
2. Create the database if it doesn't already exist:
   ```sql
   CREATE DATABASE ridesync;
   USE ridesync;
   ```
3. Open `ridesync_database.sql` in Workbench: **File → Open SQL Script**.
4. Run the whole script: click the ⚡ lightning bolt icon, or press
   **Ctrl+Shift+Enter** (Windows/Linux) / **Cmd+Shift+Enter** (Mac).
5. The script is safe to re-run — it drops and recreates its own tables
   at the start, so running it again won't conflict with a previous run.

## Connecting from Spring Boot

In `application.properties` (or `application.yml`), point your Spring Boot
backend at this database, e.g.:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ridesync
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=validate
```

`ddl-auto=validate` is recommended once the schema above is in place, so
Spring Data JPA checks your `@Entity` classes against these tables instead
of trying to auto-generate or alter them.
