// Custom functional componsents
import { deleteStudent, getAllStudents } from "./Client";
import StudentDrawerForm from "./StudentDrawerForm";
import { errorNotification, successNotification } from "./Notification";

// Styles
import './App.css';

// Functional componsents
import {
  useState,
  useEffect
} from "react";

import {
  Layout,
  Menu,
  Breadcrumb,
  Table,
  Spin,
  Empty,
  Button,
  Badge,
  Tag,
  Avatar,
  Radio,
  Popconfirm,
  Image,
  Divider
} from "antd";


// Import Icons
import {
  DesktopOutlined,
  FileOutlined,
  PieChartOutlined,
  TeamOutlined,
  UserOutlined,
  LoadingOutlined,
  PlusOutlined
} from '@ant-design/icons';



// Setup Main Layout 
const { Header, Content, Footer, Sider } = Layout;

const items = [
  getItem('Option 1', '1', <PieChartOutlined />),
  getItem('Option 2', '2', <DesktopOutlined />),
  getItem('User', 'sub1', <UserOutlined />, [
    getItem('Tom', '3'),
    getItem('Bill', '4'),
    getItem('Alex', '5'),
  ]),
  getItem('Team', 'sub2', <TeamOutlined />, [getItem('Team 1', '6'), getItem('Team 2', '8')]),
  getItem('Files', '9', <FileOutlined />),
];

function getItem(label, key, icon, children) {
  return {
    key,
    icon,
    children,
    label,
  };
}

// Add avatar in the table column
const TheAvatar = ({ name }) => {
  if (name === null) {
    if (name === null) {
      return <Avatar icon={<UserOutlined />} />
    }

    let trim = name.trim();
    const split = trim.split(" ");
    if (split.length === 1) {
      return <Avatar>{name.charAt(0)}</Avatar>
    }
  }

  return <Avatar>
    {`${name.charAt(0)}${name.charAt(name.length - 1)}`}
  </Avatar>
}

// Remove student request
const removeStudent = (studentId, callback) => {
  deleteStudent(studentId).then(() => {
    successNotification("Student deleted", `Student with ${studentId} was deleted`);
    callback();
  }).catch(err => {
    console.log(err.response);
    // get error response as an object
    err.response.json().then(res => {
      console.log(res);
      errorNotification(
        "There was an issue",
        `${res.message} [statusCode ${res.status}] [${res.statusText}]`
      )
    })
  });
}

// Setup table columns
const columns = fetchStudents => [
  {
    title: '',
    dataIndex: 'avatar',
    key: 'avatar',
    render: (text, student) => <TheAvatar name={student.name} />
  },
  {
    title: 'Id',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Email',
    dataIndex: 'email',
    key: 'email',
  },
  {
    title: 'Gender',
    dataIndex: 'gender',
    key: 'gender',
  },
  {
    title: 'Actions',
    key: 'actions',
    render: (text, student) =>
      <Radio.Group>
        <Popconfirm
          placement='topRight'
          title={`Are you sure to delete ${student.name}`}
          onConfirm={() => removeStudent(student.id, fetchStudents)}
          okText='Yes'
          cancelText='No'>
          <Radio.Button value="small">Delete</Radio.Button>
        </Popconfirm>
        <Radio.Button value="small">Edit</Radio.Button>
      </Radio.Group>
  }
];


// Setup spin loader
const antIcon = <LoadingOutlined style={{ fontSize: 24, }} spin />;


function App() {

  // Fetch students only once when the component is loaded
  useEffect(() => {
    console.log("Component is mounted");
    fetchStudents();
  }, []);

  // Setup Main Layout left vertical sidebar/menu 
  const [collapsed, setCollapsed] = useState(false);

  // Student Drawer Form vertical right sidebar/menu
  const [showDrawer, setShowDrawer] = useState(false);

  // Setup students data
  const [students, setStudents] = useState([]);
  // Setup spin indicator, while fetching data or loading something
  const [fetching, setFetching] = useState(true);

  const fetchStudents = () => getAllStudents()
    .then(response => response.json())
    .then(data => {
      console.log(data);
      // fetch students data
      setStudents(data);
    }).catch(err => {
      console.log(err.response);
      // get the error response as object from BE
      err.response.json().then(res => {
        console.log(res);
        errorNotification(
          "There was an issues",
          `${res.message} [statusCode ${res.status}] [${res.error}]`);
      });
    }).finally(() => {
      // stop the spin when the data is fetched
      setFetching(false);
    });

  // Setup Table Students data
  const renderStudentsTable = () => {
    // Run spin while the data is fetching
    if (fetching) { return <Spin indicator={antIcon} />; }

    // Check for data if is empty show add new student button hide students table and show empty bucket icon
    if (students.length <= 0) {
      return <>
        <Button
          onClick={() => setShowDrawer(!showDrawer)}
          type="primary" shape="round" icon={<PlusOutlined />} size="small">
          Add New Student
        </Button>
        <StudentDrawerForm
          showDrawer={showDrawer}
          setShowDrawer={setShowDrawer}
          fetchStudents={fetchStudents}
        />
        <Empty />
      </>
    }

    return <>

      <StudentDrawerForm
        showDrawer={showDrawer}
        setShowDrawer={setShowDrawer}
        fetchStudents={fetchStudents}
      />

      <Table
        dataSource={students}
        columns={columns(fetchStudents)}
        bordered
        title={() =>
          // <> </> Wrap more than one component
          <>
            <Tag style={{ marginLeft: "10px" }}>Number of students</Tag>
            <Badge count={students.length} className="site-badge-count-4" />
            <br /><br />
            <Button
              // Call show drawer form right vertical menu
              onClick={() => setShowDrawer(!showDrawer)}
              type="primary" shape="round" icon={<PlusOutlined />} size="small"
            > Add New Student </Button>
          </>

        }
        pagination={{ pageSize: 50 }}
        scroll={{ y: 500 }}
        rowKey={(student) => student.id}
      />

    </>
  }

  // Main Layout
  return (
    <Layout
      style={{
        minHeight: '100vh',
      }}
    >
      <Sider collapsible collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
        <div className="logo" />
        <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline" items={items} />
      </Sider>
      <Layout className="site-layout">
        <Header
          className="site-layout-background"
          style={{
            padding: 0,
          }}
        />
        <Content
          style={{
            margin: '0 16px',
          }}
        >
          <Breadcrumb
            style={{
              margin: '16px 0',
            }}
          >
            <Breadcrumb.Item>User</Breadcrumb.Item>
            <Breadcrumb.Item>Bill</Breadcrumb.Item>
          </Breadcrumb>
          <div
            className="site-layout-background"
            style={{
              padding: 24,
              minHeight: 360,
            }}
          >

            {
              // Render Student Table 
              renderStudentsTable()
            }

          </div>
        </Content>
        <Footer
          style={{
            textAlign: 'center',
          }}
        >
          <Image
            width={75}
            src="https://user-images.githubusercontent.com/45128462/206873801-7bfc2416-678f-4f67-a69b-c9cd9cc21d2c.png"
          />
          <Divider>
            <a
                rel="noopener noreferrer"
                target="_blank"
                href="http://www.syscomz.com">
                Click here!
                <br/>CI/CD test
            </a>
          </Divider>
        </Footer>
      </Layout>
    </Layout>
  )
}

export default App;
