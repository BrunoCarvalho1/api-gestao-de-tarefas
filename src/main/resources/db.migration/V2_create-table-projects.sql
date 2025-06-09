create TABLE projects (
    id TEXT PRIMARY KEY UNIQUE NOT NULL,
    name TEXT NOT NULL UNIQUE,
    tasks TEXT NOT NULL,
    role TEXT NOT NULL
);