# Examinator
The application is designed to test students on a selected topic

## Working with a project

**run application**: java -jar ./target/examiner-3.0.jar

| application properties (Application.yml) | Description                                                                                                                                          |
|:----------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------|
|              **examiner:**               |
|             exam-score-pass              | Defines the number of points needs for successfully passing an exam                                                                                  |
|              exam-data-path              | Specifies path to sub folder of resource folder where is file exams data located                                                                     |
|                  locale                  | Defines a locale the application will show exam and message data with.<br>  The application work in english data if locale unspecified in properties |
