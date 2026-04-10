# QRCode Service

A RESTful QR code generation service built with Spring Boot as part of the Hyperskill Academy project.

<p>This project utilizes QR codes — 2D barcodes that can store large amounts of data and are easily read by smartphones. Through this Spring Boot project, users can learn about the technology behind QR codes, generate them programmatically, and integrate them into a web service.</p><br/><br/>Learn more at <a href="https://hyperskill.org/projects/385?utm_source=ide&utm_medium=ide&utm_campaign=ide&utm_content=project-card">https://hyperskill.org/projects/385</a>

Here's the link to the project: https://hyperskill.org/projects/385

Check out my profile: https://hyperskill.org/profile/629764205

## Features
- Health check endpoint: `GET /api/health`
- QR code generation endpoint: `GET /api/qrcode`
- Supports image formats: `png`, `jpeg`, `gif`
- Supports configurable image size (150 to 350 pixels)
- Supports QR error correction levels: `L`, `M`, `Q`, `H`
- Applies default values when optional params are missing:
  - `size=250`
  - `correction=L`
  - `type=png`
- Validates request parameters with clear JSON error messages
- Enforces validation priority:
  `contents` > `size` > `correction` > `type`

## Tech Stack
- Java
- Spring Boot
- ZXing (QR code generation)
- Gradle

This project demonstrates API design, validation strategy, and image generation in a production-style Spring Boot service.
