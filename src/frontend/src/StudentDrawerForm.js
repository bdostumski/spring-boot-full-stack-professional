import { useState } from 'react';
import { addNewStudent } from './Client';
import { successNotification, errorNotification } from './Notification';

import {
    Drawer,
    Input,
    Col,
    Select,
    Form,
    Row,
    Spin,
    Button, 
} from 'antd';

import {
    LoadingOutlined
} from '@ant-design/icons';

const { Option } = Select;
const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

// This is the right vertical menu with form to add new students
// Functional component StudnetDrawerForm with showDrawer, setShowDrawer, and fetchStudents properties
function StudentDrawerForm({ showDrawer, setShowDrawer, fetchStudents }) {

    const onCLose = () => setShowDrawer(false);
    const [submitting, setSubmitting] = useState(false);

    const onFinish = student => {
        setSubmitting(true);
        console.log(JSON.stringify(student, null, 2))
        addNewStudent(student)
            .then(() => {
                console.log("student added");
                onCLose();
                successNotification("Student succesfully added", `${student.name} was added to the system`)
                fetchStudents();
            }).catch(err => {
                console.log(err.response);
                // get error response as an object
                err.response.json().then(res => {
                    errorNotification(
                        "There was an issue",
                        `${res.message} [statusCode ${res.status}] [${res.statusText}]`,
                        "bottomLeft");
                });
            }).finally(() => {
                setSubmitting(false);
            })
    };

    const onFinishFailed = errorInfo => {
        alert(JSON.stringify(errorInfo, null, 2));
    };

    return (
        <Drawer
            title="Create new student"
            width={720}
            onClose={onCLose}
            open={showDrawer}
            bodyStyle={{ paddingBottom: 80 }}
            footer={
                <div
                    style={{
                        textAlign: 'right',
                    }}
                >
                    <Button onClick={onCLose} style={{ marginRight: 8 }}>
                        Cancel
                    </Button>
                </div>
            }
        >
            <Form layout="vertical"
                onFinishFailed={onFinishFailed}
                onFinish={onFinish}
                hideRequiredMark>
                <Row gutter={16}>
                    <Col span={12}>
                        <Form.Item
                            name="name"
                            label="Name"
                            rules={[{ required: false, message: 'Please enter student name' }]}
                        >
                            <Input placeholder="Please enter student name" />
                        </Form.Item>
                    </Col>
                    <Col span={12}>
                        <Form.Item
                            name="email"
                            label="Email"
                            rules={[{ required: true, message: 'Please enter student email' }]}
                        >
                            <Input placeholder="Please enter student email" />
                        </Form.Item>
                    </Col>
                </Row>
                <Row gutter={16}>
                    <Col span={12}>
                        <Form.Item
                            name="gender"
                            label="gender"
                            rules={[{ required: true, message: 'Please select a gender' }]}
                        >
                            <Select placeholder="Please select a gender">
                                <Option value="MALE">MALE</Option>
                                <Option value="FEMALE">FEMALE</Option>
                                <Option value="OTHER">OTHER</Option>
                            </Select>
                        </Form.Item>
                    </Col>
                </Row>
                <Row>
                    <Col span={12}>
                        <Form.Item >
                            <Button type="primary" htmlType="submit">
                                Submit
                            </Button>
                        </Form.Item>
                    </Col>
                </Row>
                <Row>
                    {submitting && <Spin indicator={antIcon} />}
                </Row>
            </Form>
        </Drawer>
    )
}

export default StudentDrawerForm;